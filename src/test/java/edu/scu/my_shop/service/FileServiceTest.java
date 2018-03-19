package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Image;
import edu.scu.my_shop.exception.FileException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static edu.scu.my_shop.exception.FileException.QUERY_PID_ERROR;
import static org.junit.Assert.*;

/**
 * Created by Vicent_Chen on 2018/3/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    public void getProductImagesPath() {
        List<String> pathList = fileService.getProductImagesURL("test-for-image");
        for (String path : pathList) {
            System.out.println(path);
        }

        pathList = fileService.getProductImagesURL("hello");
        assertEquals(true, pathList.isEmpty());

        try {
            pathList = fileService.getProductImagesURL(null);
        } catch (FileException e){
            assertEquals(QUERY_PID_ERROR, e.getCode().longValue());
        }
    }

    @Test
    public void deleteImage() {
        Image image = new Image();
        image.setImageUrl("29b4c91a-318f-490f-a37e-62c540fe0c80");
        fileService.deleteProductImage(image);

        image.setImageId("4e50c66d-8042-4829-975f-62ddc885e7c0");
        image.setImageUrl("4e50c66d-8042-4829-975f-62ddc885e7c0");
        fileService.deleteProductImage(image);
    }

    @Test
    public void updateImage() {
        Image image = new Image();
        image.setImageId("450db553-5992-45e3-8d85-cac5af613a44");
        image.setImageUrl("450db553-5992-45e3-8d85-cac5af613a44");
    }
}