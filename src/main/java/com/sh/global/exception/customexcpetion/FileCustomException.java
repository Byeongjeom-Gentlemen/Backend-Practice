package com.sh.global.exception.customexcpetion;

import com.sh.global.exception.errorcode.FileErrorCode;

public class FileCustomException extends CustomException {

    public static final FileCustomException IS_NOT_IMAGE =
            new FileCustomException(FileErrorCode.IS_NOT_IMAGE);

    public static final FileCustomException FILE_DOES_NOT_EXIST =
            new FileCustomException(FileErrorCode.File_DOES_NOT_EXIST);

    public FileCustomException(FileErrorCode errorCode) {
        super(errorCode);
    }
}
