package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.UserMapper;
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
     * Write file to disk.
     * @param fileAbsolutePath: The <b>absolute</b> path of file
     * @param file: file uploaded from browser
     */
    private void uploadFile(String fileAbsolutePath, MultipartFile file) {
        // TODO: log
        if(file.isEmpty()) {
            throw new FileException("File is empty", FileException.EMPTY_FILE);
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
            String error = "Cannot write file: " + e.getMessage();
            throw new FileException(error, FileException.IO_EXCEPTION);
        }
    }

    /**
     * Return the related URL of user image
     * @param userID
     * @return
     */
    public String getUserImagePath(String userID) {
        return userImageRelatePath + "/" + userID;
    }
}
