package ch.zhaw.deeplearningjava.sports_classification;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ClassificationController {

    private final Inference inference;

    public ClassificationController() throws Exception {
        this.inference = new Inference();
    }

    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws IOException, TranslateException {
        Classifications result = inference.predict(image.getBytes());
        return result.toJson();
    }

    @GetMapping("/ping")
    public String ping() {
        return "Image classification app is up and running!";
    }
}
