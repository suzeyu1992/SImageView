package com.szysky.customize.siv.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午4:56
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class CloseUtil {

    public static void close(Object stream){
        if ((stream instanceof InputStream) ){
            try {
                ((InputStream)stream).close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ((stream instanceof OutputStream)){
            try {
                ((OutputStream)stream).close();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
