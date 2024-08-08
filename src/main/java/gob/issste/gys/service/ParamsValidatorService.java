package gob.issste.gys.service;

import gob.issste.gys.JdbcTemplateDemo01Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamsValidatorService {

    Logger logger = LoggerFactory.getLogger(JdbcTemplateDemo01Application.class);
    private final String[] sqlInjections = new String[] {
            ";", "--", "'", "/*", "*/", "@@", "@", "NULL", "TRUE", "FALSE", "NOT", "AND", "OR",
            "IS", "BETWEEN", "LIKE", "IN", "EXISTS", "ALL", "ANY", "SOME", "WITHIN", "INTERSECT",
            "COLLATE", "AS", "ON", "USING", "JOIN", "CROSS", "INNER", "OUTER", "LEFT", "RIGHT",
            "NATURAL", "WHERE", "ORDER BY", "GROUP BY", "HAVING", "LIMIT", "OFFSET", "SELECT",
            "INSERT", "UPDATE", "DELETE", "DROP", "EXEC", "EXECUTE", "ALTER", "CREATE", "SHUTDOWN",
            "GRANT", "REVOKE", "UNION", "LOAD_FILE", "INTO OUTFILE", "INTO DUMPFILE"
    };
    public boolean sqlInjectionObjectValidator(List<String> inputData) {
        List<String> list = new ArrayList<>();
        for (String inputDataElement : inputData) {
            if(inputDataElement != null)
             for (String sqlInjection : sqlInjections) {
                if (inputDataElement.toUpperCase().contains(sqlInjection)) {
                    list.add(inputDataElement.toUpperCase());
                }
            }
        }
        return list.toArray().length != 0;
    }

    public boolean regexMultipleDataValidation(List<String> inputData, List<String> regexList) {
        List<String> rgexMatchingData = new ArrayList<>();
        Pattern pattern;
        Matcher matcher;
        for (String param: inputData) {
            if (param != null)
                for (String regexValue : regexList) {
                    pattern = Pattern.compile(regexValue);
                    matcher = pattern.matcher(param);
                    if (!matcher.matches()){
                        rgexMatchingData.add(param);
                    }
                }
        }
        return rgexMatchingData.toArray().length != 0;
    }
    public boolean regexValidation(String regex, String value){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

}


