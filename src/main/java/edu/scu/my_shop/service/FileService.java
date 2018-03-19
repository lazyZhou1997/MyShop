package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.ImageMapper;
import edu.scu.my_shop.dao.ProductMapper;
import edu.scu.my_shop.dao.UserMapper;
import edu.scu.my_shop.entity.Image;
import edu.scu.my_shop.entity.ImageExample;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.exception.FileException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static edu.scu.my_shop.entity.ImageExample.*;
import static edu.scu.my_shop.exception.FileException.*;

/**
 * Created by Vicent_Chen on 2018/3/18.
 * FIXME: CANNOT access image before reboot.
 */
@Service
public class FileService {

    @Value("${image.user-image-folder-path}")
    private String userImageFolderPath;

    @Value("${image.user-image-relate-path}")
    private String userImageRelatePath;

    @Value("${image.product-image-folder-path}")
    private String productImageFolderPath;

    @Value("${image.product-image-relate-path}")
    private String productImageRelatePath;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    /**
     * Upload user image and update database.
     * @param userID
     * @param file
     */
    public void uploadUserImage(String userID, MultipartFile file) {

        // currently use userID as file name
        String fileName = userID;

        // rename file
        String fileAbsolutePath = userImageFolderPath + "/" + fileName;
        System.out.println(fileAbsolutePath);

        // write file to disk
        uploadFile(fileAbsolutePath, file);

        // update database(optional)
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectByPrimaryKey(userID);
        user.setHeadImg(userID);
        userMapper.updateByPrimaryKey(user);
        sqlSession.close();
    }

    /**
     * Write multiple files to disk.
     * Image ID will be automatically generated.
     * <b>DO NOT</b> invoke this method to <b>UPDATE</b> image.
     * @param productID
     * @param files
     */
    public void insertProductImages(String productID, List<MultipartFile> files) {

        // check if files are null
        if (files == null || files.isEmpty()) {
            throw new FileException(EMPTY_FILE_MESSAGE, EMPTY_FILE);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);

        for (MultipartFile file : files) {
            // rename file
            String fileName = UUID.randomUUID().toString();
            String fileAbsolutePath = productImageFolderPath + "/" + fileName;
            uploadFile(fileAbsolutePath, file);

            // write to database
            Image image = new Image();
            image.setImageId(fileName); image.setImageUrl(fileName); image.setProductId(productID);
            imageMapper.insert(image);
        }

        sqlSession.close();
    }

    public void updateProductImages(List<String> imageIDs, List<MultipartFile> files) {

        // validate data
        if (imageIDs == null || imageIDs.isEmpty()) {
            throw new FileException(IMAGE_ID_EMPTY_MESSAGE, IMAGE_ID_EMPTY);
        }
        if (files == null || files.isEmpty()) {
            throw new FileException(EMPTY_FILE_MESSAGE, EMPTY_FILE);
        }
        if (imageIDs.size() != files.size()) {
            throw new FileException(IMAGE_ID_FILE_NOT_MATCH_MESSAGE, IMAGE_ID_FILE_NOT_MATCH);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);

        for (int i = 0; i < imageIDs.size(); i++) {
            Image image = imageMapper.selectByPrimaryKey(imageIDs.get(i));
            MultipartFile file = files.get(i);

            // pass empty file in case that front-end send empty
            if (file == null || file.isEmpty()) continue;

            // write to disk
            String fileName = image.getImageUrl();
            String fileAbsolutePath = productImageFolderPath + "/" + fileName;
            uploadFile(fileAbsolutePath, file);
        }

        sqlSession.close();
    }

    /**
     * Delete image and automatically update database.
     * Property ID and URL in the image must not be null.
     * @param image
     */
    public void deleteProductImage(Image image) {
        String imageID = image.getImageId();
        String imageURL = image.getImageUrl();

        // validate image data
        if (imageID == null || imageID.equals("")) {
            throw new FileException(IMAGE_ID_EMPTY_MESSAGE, IMAGE_ID_EMPTY);
        }
        if (imageURL == null || imageURL.equals("")) {
            throw new FileException(IMAGE_URL_EMPTY_MESSAGE, IMAGE_URL_EMPTY);
        }

        // delete file
        String fileAbsolutePath =  productImageFolderPath + "/" + imageURL;
        deleteFile(fileAbsolutePath);

        // update database
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
        imageMapper.deleteByPrimaryKey(imageID);
        sqlSession.close();
    }

    /**
     * Delete image and automatically update database.
     * @param imageID
     */
    public void deleteProductImage(String imageID) {
        // validate data
        if (imageID == null || imageID.equals("")) {
            throw new FileException(IMAGE_ID_EMPTY_MESSAGE, IMAGE_ID_EMPTY);
        }

        // query image from database
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
        Image image = imageMapper.selectByPrimaryKey(imageID);
        sqlSession.close();

        // delete file and automatically update database
        deleteProductImage(image);
    }

    /**
     * Delete a list of images.
     * <b>If imageList is null, it will do nothing.</b>
     * @param imageList
     */
    public void deleteProductImages(List<Image> imageList) {
        // verify data
        if (imageList == null) {
            return;
        }

        for (Image image : imageList) {
            deleteProductImage(image);
        }
    }

    /**
     * Delete all images that belongs to productID.
     * @param productID
     */
    public void deleteAllProductImages(String productID) {
        // validate data
        if (productID == null || productID.equals("")) {
            throw new FileException(QUERY_PID_ERROR_MESSAGE, QUERY_PID_ERROR);
        }

        // create search example
        ImageExample imageExample = new ImageExample();
        Criteria criteria = imageExample.createCriteria();
        criteria.andProductIdEqualTo(productID);

        // search all images
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
        List<Image> imageList = imageMapper.selectByExample(imageExample);
        sqlSession.close();

        for (Image image : imageList) {
            deleteProductImage(image);
        }
    }

    /**
     * Return the related URL of user image
     * @param userID
     * @return
     */
    public String getUserImageURL(String userID) {
        return userImageRelatePath + "/" + userID;
    }

    /**
     * Get all images related URL of product.
     * It <b>WILL NOT</b> return null but an empty list.
     * @param productID
     * @return
     */
    public List<String> getProductImagesURL(String productID) {
        if (productID == null || productID.equals("")) {
            throw new FileException(QUERY_PID_ERROR_MESSAGE, QUERY_PID_ERROR);
        }

        // extract urls
        List<Image> images = getProductImages(productID);
        List<String> pathList = new ArrayList<>();
        for (Image image : images) {
            pathList.add(productImageRelatePath + "/" + image.getImageUrl());
        }

        return pathList;
    }

    /**
     * Query images by product ID.
     * @param productID
     * @return
     */
    private List<Image> getProductImages(String productID) {
        if (productID == null || productID.equals("")) {
            throw new FileException(QUERY_PID_ERROR_MESSAGE, QUERY_PID_ERROR);
        }

        // search for database
        ImageExample imageExample = new ImageExample();
        Criteria criteria = imageExample.createCriteria();
        criteria.andProductIdEqualTo(productID);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        ImageMapper imageMapper = sqlSession.getMapper(ImageMapper.class);
        List<Image> images = imageMapper.selectByExample(imageExample);
        sqlSession.close();

        return images;
    }

    /**
     * Write file to disk.
     * @param fileAbsolutePath: The <b>absolute</b> path of file
     * @param file: file uploaded from browser
     */
    private void uploadFile(String fileAbsolutePath, MultipartFile file) {
        // TODO: log
        if (fileAbsolutePath == null || fileAbsolutePath.equals("")) {
            throw new FileException(IO_EXCEPTION_MESSAGE, IO_EXCEPTION);
        }
        if(file.isEmpty()) {
            throw new FileException(EMPTY_FILE_MESSAGE, EMPTY_FILE);
        }

        // check if directory exists
        File fileToSave = new File(fileAbsolutePath);
        if(!fileToSave.getParentFile().exists()){
            fileToSave.getParentFile().mkdir();
        }

        // write file
        try {
            file.transferTo(fileToSave);
        } catch (IOException e) {
            String error = "不能写入文件: " + e.getMessage();
            throw new FileException( IO_EXCEPTION_MESSAGE + error, IO_EXCEPTION);
        }
    }

    /**
     * Delete file.
     * @param fileAbsolutePath
     */
    private void deleteFile(String fileAbsolutePath) {
        if (fileAbsolutePath == null || fileAbsolutePath.equals("")) {
            throw new FileException(IO_EXCEPTION_MESSAGE, IO_EXCEPTION);
        }

        File fileToDelete = new File(fileAbsolutePath);
        // ignore file that not exists or that is a directory
        if (!fileToDelete.exists() || !fileToDelete.isFile()) {
            return;
        }

        fileToDelete.delete();
    }
}
