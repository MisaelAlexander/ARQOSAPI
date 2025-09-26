package PTC._5.Controllers.CloudinaryController;

import PTC._5.Services.Cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/Imagen")
@CrossOrigin
public class CloudinaryController
{

    @Autowired
    private final CloudinaryService service;

    public CloudinaryController(CloudinaryService service) {
        this.service = service;
    }

    @PostMapping("/Guardar")

    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) {
        try{
            //Llamando al servicio para subir la imagen y obtener la URL
            String imageUrl = service.uploadImage(file);
            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida exitosamente",
                    "url", imageUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    @PostMapping("/GuardarCarpeta")
    public ResponseEntity<?> uploadImageToFolder(
            @RequestParam("image") MultipartFile file,
            @RequestParam String folder
    ){
        try{
            String imageUrl = service.uploadImage(file, folder);
            return ResponseEntity.ok(Map.of(
                    "message", "Imagen subida exitosamente",
                    "url", imageUrl
            ));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }
}
