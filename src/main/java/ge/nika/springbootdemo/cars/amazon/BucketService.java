package ge.nika.springbootdemo.cars.amazon;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;


@Service
public class BucketService implements BucketRepository{

    private static final Logger log = LoggerFactory.getLogger(BucketService.class);


    private String bucketName;
    private String folderName;

    private AmazonS3 s3Client;

    public BucketService( //made a custom constructor because @Value wont work with lombok
            @Value("${cloud.aws.s3.bucket-name}") String bucketName,
            @Value("${cloud.aws.s3.folder-name}") String folderName,
            AmazonS3 s3Client) {
        this.bucketName = bucketName;
        this.folderName = folderName;
        this.s3Client = s3Client;
    }

    @Override
    public String getImageURLFromBucket(String carName){
        String objectKey = folderName + "/" + carName;

        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);

        //generating a pre signed URL request
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }

    @Override
    public String putImageIntoBucket(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = folderName + "/" + file.getOriginalFilename();

            /* Set metadata to indicate it's an image */
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType()); // Ensure correct MIME type (e.g., image/jpeg)

            // Upload the image with metadata
            s3Client.putObject(bucketName, fileName, inputStream, metadata);

            return "File uploaded: " + fileName;
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            return "Upload failed";
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }catch (IOException exception){
            log.error("error converting a Multipartfile to File", exception);
        }
        return convertedFile;
    }
}
