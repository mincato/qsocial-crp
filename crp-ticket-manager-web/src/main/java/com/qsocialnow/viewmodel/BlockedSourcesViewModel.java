package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Media;

@VariableResolver(DelegatingVariableResolver.class)
public class BlockedSourcesViewModel implements Serializable {

    private static final long serialVersionUID = 7775828367080466880L;

    private List<Media> sources;

    public List<Media> getSources() {
        return sources;
    }

    @Init
    public void init(@BindingParam("blockedSources") Set<Long> blockedSources) {
        sources = blockedSources.stream().map(blockedSource -> Media.getByValue(blockedSource))
                .collect(Collectors.toList());
    }

}
