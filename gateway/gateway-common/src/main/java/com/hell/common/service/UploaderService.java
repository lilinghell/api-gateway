package com.hell.common.service;

import com.hell.common.CheckMsg;
import com.hell.db.dao.common.AttachmentDao;
import com.hell.db.table.common.PAttachment;
import com.hell.core.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class UploaderService {
    private AttachmentDao attachmentDao;
    @Value("${diga.static.root.path}")
    private String staticRootPath;

    public PAttachment singleUpload(String path, MultipartFile fileUpload) throws ValidationException {
        try {
            String fileName = fileUpload.getOriginalFilename();
            String newFileName = "";
            if (fileName.lastIndexOf(".") != -1) {
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                newFileName = UUID.randomUUID().toString() + suffixName;
            } else {
                newFileName = UUID.randomUUID().toString();
            }

            PAttachment p = new PAttachment();
            p.setLocal_name(newFileName);
            p.setView_name(fileName);
            p.setSize(String.valueOf(fileUpload.getSize()));
            p.setPath(path);

            File file = new File(this.staticRootPath + path + newFileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fileUpload.transferTo(file);

            PAttachment p2 = attachmentDao.save(p);
            return p2;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException(CheckMsg.VALIDATION_UPLOAD_ERROR);
        }
    }

    @Autowired
    public void setAttachmentDao(AttachmentDao attachmentDao) {
        this.attachmentDao = attachmentDao;
    }
}
