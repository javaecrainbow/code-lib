package com.salk.lib.tool.antrl.g4.visitor;

import com.salk.lib.tool.antrl.g4.MySqlParser;
import com.salk.lib.tool.antrl.g4.MySqlParserBaseVisitor;

/**
 * @author salkli
 * @since 2022/11/25
 **/
public class TableNamesVisitor extends MySqlParserBaseVisitor<String> {
    @Override
    public String visitTableName(MySqlParser.TableNameContext ctx) {
        return ctx.getText();
    }
}
