package com.berlioz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Environment {
    private static Logger logger = LogManager.getLogger(Environment.class);

    private Map<String,String> _variables = new HashMap<String, String>();

    public Environment()
    {
        Map<String,String> origEnv = System.getenv();
        for (Map.Entry<String, String> entry : origEnv.entrySet())
        {
            String value = entry.getValue();
            value = this._massageValue(value, origEnv);
            _variables.put(entry.getKey(), value);
        }

        for (Map.Entry<String, String> entry : _variables.entrySet())
        {
            logger.info("ENVIRONMENT {} :=> {}", entry.getKey(), entry.getValue());
        }
    }

    public String get(String value)
    {
        return _variables.get(value);
    }

    private String _massageValue(String value, Map<String,String> origEnv)
    {
        String pattern = "\\$\\{(\\w*)\\}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (m.find())
        {
            String targetVarName = m.group(1);
            if (origEnv.containsKey(targetVarName)) {
                m.appendReplacement(sb, origEnv.get(targetVarName) );
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
