package com.p6spy.engine.spy;

import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.option.P6OptionsRepository;

public interface P6Factory {

	P6LoadableOptions getOptions(P6OptionsRepository optionsRepository);

	JdbcEventListener getJdbcEventListener();

}