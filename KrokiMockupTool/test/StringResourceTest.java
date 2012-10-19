
import kroki.app.utils.StringResource;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vladan Marsenić (vladan.marsenic@gmail.com)
 */
public class StringResourceTest {

    public static void main(String[] args){
        String s = StringResource.getStringResource("action.addReport.smallImage");
        System.out.println(s);
    }

}
