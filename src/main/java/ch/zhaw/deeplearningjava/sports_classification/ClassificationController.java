package ch.zhaw.deeplearningjava.sports_classification;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ClassificationController {

    private Inference inference = new Inference();

    @GetMapping("/ping")
    public String ping() {
        return "ClassificationApp is up and running!";
    }
    
    @PostMapping(path = "/analyze")
    public String predict(@RequestParam("image") MultipartFile image) throws Exception {
        System.out.println(image);
        return inference.predict(image.getBytes()).toJson();
    }
}
