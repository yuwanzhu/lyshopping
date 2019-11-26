package com.lyshopping.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuying
 * 文件上传
 **/
public interface IFileService {
    String upload(MultipartFile file, String path);
}
