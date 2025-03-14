package ge.nika.springbootdemo.cars.amazon;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BucketRepository {

    //download
    String getImageURLFromBucket(String carName)throws IOException;

    public String putImageIntoBucket(MultipartFile file);
}
