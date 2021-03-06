package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.ScalarEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RootValidator extends YsldValidateHandler {

    // Colour Expressions
    static Pattern COLOR = Pattern.compile("(?:fill|stroke)-color");
    // Name expressions
    static Pattern NAME = Pattern.compile("shape|font-(?:family|style|weight)|stroke(?:linecap|linejoin)");
    // General expressions
    static Pattern EXPRESSION = Pattern.compile("stroke-(?:width|opacity|linejoin|linecap|dashoffset)|offset|shape|gamma|geometry|label|font-(?:family|size|style|weight)|size|rotation|gap|initial-gap|radius|opacity");
    
    @Override
    public void scalar(ScalarEvent evt, YsldValidateContext context) {
        String key = evt.getValue();
        
        Matcher m = COLOR.matcher(key);
        if (m.matches()) {
            context.push(new ColorValidator());
        } else if ("filter".equals(key)) {
            context.push(new FilterValidator());
        } else if (NAME.matcher(key).matches()) {
            context.push(new ExpressionValidator());
        } else if (EXPRESSION.matcher(key).matches()) {
            context.push(new ExpressionValidator());
        }  else if ("grid".equals(key)) {
            context.push(new GridValidator());
        }
    }
    
}
