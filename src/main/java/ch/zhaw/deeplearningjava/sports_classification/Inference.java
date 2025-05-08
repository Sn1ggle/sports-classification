package ch.zhaw.deeplearningjava.sports_classification;

import ai.djl.Application;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import ai.djl.inference.Predictor;

public class Inference {

    private final Predictor<Image, Classifications> predictor;

    public Inference() throws Exception {
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                .optFilter("layers", "50")   // ResNet50
                .optFilter("flavor", "v1")   // Version 1
                .optProgress(new ProgressBar())
                .build();

        ZooModel<Image, Classifications> model = criteria.loadModel();
        predictor = model.newPredictor();
    }

    public Classifications predict(byte[] imageData) throws IOException, TranslateException {
        InputStream is = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(is);
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        return predictor.predict(img);
    }
}
