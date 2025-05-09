package ch.zhaw.deeplearningjava.sports_classification;

import ai.djl.Model;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.metric.Metrics;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingConfig;
import ai.djl.training.TrainingResult;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Adam;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.translate.TranslateException;


import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Training {

    private static final int BATCH_SIZE = 32;
    private static final int EPOCHS = 15;

    public static void main(String[] args) throws IOException, TranslateException {
        Path modelDir = Paths.get("models");

        System.out.println("Lade Dataset...");
        ImageFolder dataset = initDataset("sports-classification-20/train");
        RandomAccessDataset[] datasets = dataset.randomSplit(8, 2);

        System.out.println("Erstelle Modell...");
        Model model = Models.getModel();

        Loss loss = Loss.softmaxCrossEntropyLoss();
        TrainingConfig config = setupTrainingConfig(loss);

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.setMetrics(new Metrics());
            Shape inputShape = new Shape(1, 3, Models.IMAGE_HEIGHT, Models.IMAGE_WIDTH);
            trainer.initialize(inputShape);

            System.out.println("Starte Training...");
            EasyTrain.fit(trainer, EPOCHS, datasets[0], datasets[1]);

            TrainingResult result = trainer.getTrainingResult();
            model.setProperty("Epoch", String.valueOf(EPOCHS));
            model.setProperty("Accuracy", String.format("%.5f", result.getValidateEvaluation("Accuracy")));
            model.setProperty("Loss", String.format("%.5f", result.getValidateLoss()));

            System.out.println("Speichere Modell...");
            model.save(modelDir, Models.MODEL_NAME);
            Models.saveSynset(modelDir, dataset.getSynset());
            System.out.println("Modell gespeichert in /models");
        }
    }

    private static ImageFolder initDataset(String datasetRoot) throws IOException, TranslateException {
        ImageFolder dataset = ImageFolder.builder()
                .setRepositoryPath(Paths.get(datasetRoot))
                .optMaxDepth(10)
                .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                .addTransform(new ToTensor())
                .setSampling(BATCH_SIZE, true)
                .build();

        dataset.prepare();
        return dataset;
    }

    private static TrainingConfig setupTrainingConfig(Loss loss) {
/*
        Optimizer optimizer = Adam.builder()
                .optLearningRateTracker(Tracker.fixed(0.001f))
                .build();
*/
        return new DefaultTrainingConfig(loss)
//                .optOptimizer(optimizer)
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());
    }
}

