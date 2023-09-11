package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.FileErrorCode;

public class FileCustomException extends CustomException {

    public static final FileCustomException IS_NOT_IMAGE =
            new FileCustomException(FileErrorCode.IS_NOT_IMAGE);

    public static final FileCustomException FILE_DOES_NOT_EXIST =
            new FileCustomException(FileErrorCode.FILE_DOES_NOT_EXIST);

    public static final FileCustomException FILE_DOES_NOT_EXIST_REQUEST_VALUE =
            new FileCustomException(FileErrorCode.FILE_DOES_NOT_EXIST_REQUEST_VALUE);

    public static final FileCustomException FAILED_UPLOAD_FILE =
            new FileCustomException(FileErrorCode.FAILED_UPLOAD_FILE);

    public FileCustomException(FileErrorCode errorCode) {
        super(errorCode);
    }
}
