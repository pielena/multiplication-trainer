package com.github.pielena.multiplication.trainer.config;

import com.github.pielena.multiplication.trainer.appcontainer.api.AppComponent;
import com.github.pielena.multiplication.trainer.appcontainer.api.AppComponentsContainerConfig;
import com.github.pielena.multiplication.trainer.services.EquationPreparer;
import com.github.pielena.multiplication.trainer.services.EquationPreparerImpl;
import com.github.pielena.multiplication.trainer.services.GameProcessor;
import com.github.pielena.multiplication.trainer.services.GameProcessorImpl;
import com.github.pielena.multiplication.trainer.services.IOService;
import com.github.pielena.multiplication.trainer.services.IOServiceStreams;
import com.github.pielena.multiplication.trainer.services.PlayerService;
import com.github.pielena.multiplication.trainer.services.PlayerServiceImpl;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }

}
