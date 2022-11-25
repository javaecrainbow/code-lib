package com.salk.lib.tool.antrl.g4;

/**
 * @author salkli
 * @since 2022/11/24
 **/
public class FullColumnNameFieldVisitor extends MySqlParserBaseVisitor<String> {
    @Override
    public String visitFullColumnName(MySqlParser.FullColumnNameContext ctx) {
        return "this is fullColumnName expression";
        // return super.visitFullColumnName(ctx);
    }
}
