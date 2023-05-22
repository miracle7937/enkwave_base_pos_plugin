package Var;

import com.Enkpay.alltransctionsPOS.nibbs.model.NibsUtilityData;

public class Debug {

    static public void print(Object value) {
        if(NibsUtilityData.debug){
            System.out.println(value);
        }
    }

}