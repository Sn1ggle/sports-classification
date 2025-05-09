package ch.zhaw.deeplearningjava.sports_classification;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class Inference {

    private final Predictor<Image, Classifications> predictor;

    public Inference() throws Exception {
        // Modell laden
//        Model model = Model.newInstance(Models.MODEL_NAME); //Allgemeines Modell
        Model model = Models.getModel(); //Modell mit garantiert derselen Architektur wie beim Training

        model.load(Paths.get("models"), Models.MODEL_NAME);

        // DJL Translator
        Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                .addTransform(new ai.djl.modality.cv.transform.Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                .addTransform(new ai.djl.modality.cv.transform.ToTensor())
                .optApplySoftmax(true)
                .build();

        predictor = model.newPredictor(translator);
    }

    public Classifications predict(byte[] imageData) throws IOException, TranslateException {
        InputStream is = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(is);
        Image img = ImageFactory.getInstance().fromImage(bufferedImage);
        return predictor.predict(img);
    }
}

