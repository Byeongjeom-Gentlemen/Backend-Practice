package com.sh.global.exception.customexcpetion.page;

import com.sh.global.exception.PageErrorCode;
import com.sh.global.exception.customexcpetion.CustomException;

import javax.swing.undo.CannotUndoException;

public class SizeRangeOverException extends CustomException {

    public SizeRangeOverException(PageErrorCode errorCode) {
        super(errorCode);
    }
}
