package com.salk.lib.tool.antrl.g4.listener;

import com.salk.lib.tool.antrl.g4.MySqlParser;
import com.salk.lib.tool.antrl.g4.MySqlParserBaseListener;

/**
 * @author salkli
 * @since 2022/11/25
 **/
public class ColumnNamesListener extends MySqlParserBaseListener {

    @Override
    public void enterSelectColumnElement(MySqlParser.SelectColumnElementContext ctx) {
        MySqlParser.FullColumnNameContext fullColumnNameContext = ctx.fullColumnName();
        super.enterSelectColumnElement(ctx);
    }
}
