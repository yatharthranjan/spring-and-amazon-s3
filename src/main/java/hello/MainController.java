package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import java.util.List;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.Upload;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ObjectListing;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import java.io.*;


import hello.User;
import hello.UserRepository;


@Controller
@RequestMapping(path="/demo")
public class MainController{
  @Autowired
  private UserRepository userRepository;

  @GetMapping(path="/add")
  public @ResponseBody String addNewUser (@RequestParam String name
                    , @RequestParam String email) {

          User n =new User();
          n.setName(name);
          n.setEmail(email);
          userRepository.save(n);
          return "Saved";
          }


   @GetMapping(path="/all")
   public @ResponseBody Iterable<User> getAllUsers() {

     return userRepository.findAll();
   }

   @GetMapping(path="/viewbuckets")
   public @ResponseBody String getPicUrlFromAmazonS3(){
     String picUrl = "hello";

    try{
     AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
     AmazonS3 s3 = builder.withRegion("eu-west-2").build();

     List<Bucket> buckets = s3.listBuckets();
      System.out.println("Your Amazon S3 buckets are:");
      for (Bucket b : buckets) {
          System.out.println("* " + b.getName());
          picUrl = picUrl + "\n" + b.getName() ;
        }
      }
      /*catch(Exception e){
        picUrl = picUrl + e.getMessage();
      }*/
      catch(AmazonServiceException e2){
        picUrl = picUrl + e2.getMessage();
      }
      catch(AmazonClientException e3){
        picUrl = picUrl + e3.getMessage();
      }

     return picUrl;
   }

   // Request with the file name you want to create and with the key of the
   // Amazon S3 bucket Object.
   @ResponseBody
   @RequestMapping(value = "/viewpic", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
   public byte[] getPhoto(@RequestParam String fname
                     , @RequestParam String key) throws IOException{
     byte [] im = null;
     File f = new File("./" + fname);
     if (!f.exists( ))
        {
          f.createNewFile( );
          TransferManager tm = new TransferManager();
          try {
            Download dn = tm.download("spring-mysql-test-bucket",key,f);
            dn.waitForCompletion();
          }
          catch(AmazonServiceException e){
            System.err.println(e.getErrorMessage());
          }catch(AmazonClientException e){
              System.err.println(e.getMessage());
          }catch(InterruptedException e){
              System.err.println(e.getMessage());
          }
          tm.shutdownNow();
        }
     try{
       InputStream in = new FileInputStream(f);
       im = IOUtils.toByteArray(in);
     }
     catch(Exception e){
       e.printStackTrace();
       System.exit(1);
     }
     return im;
   }

   @ResponseBody
   @RequestMapping(path="/upload")
   public String phototUpload(){
     File f = new File("./me.jpg");
     if (f.exists( ))
        {
          TransferManager tm = new TransferManager();
          try {
            Upload up = tm.upload("spring-mysql-test-bucket","me.jpg",f);
            up.waitForCompletion();
          }
          catch(AmazonServiceException e){
            System.err.println(e.getErrorMessage());
          }catch(AmazonClientException e){
              System.err.println(e.getMessage());
          }catch(InterruptedException e){
              System.err.println(e.getMessage());
          }
          tm.shutdownNow();
        }
     return "Saved";
   }

   @ResponseBody
   @RequestMapping(path="/photourls")
   public List<S3ObjectSummary> getPhotoUrls(){
     try{
    AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
    AmazonS3 s3 = builder.withRegion("eu-west-2").build();
      ObjectListing ol = s3.listObjects("spring-mysql-test-bucket");
      List<S3ObjectSummary> objects = ol.getObjectSummaries();
      List<S3ObjectSummary> images = new ArrayList<S3ObjectSummary>();
      for (S3ObjectSummary os: objects) {
          System.out.println("* " + os.getKey());
          if(os.getKey().contains(".png")){
            images.add(os);
          }
      }
      return images;
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return null;
   }
}
