package org.geogig.geoserver.web.repository;

import java.io.IOException;

import org.apache.wicket.model.LoadableDetachableModel;
import org.geogig.geoserver.config.RepositoryInfo;
import org.geogig.geoserver.config.RepositoryManager;

import com.google.common.base.Throwables;

/**
 * A RepositoryInfo detachable model that holds the store id to retrieve it on demand from the
 * catalog
 */
public class RepositoryInfoDetachableModel extends LoadableDetachableModel<RepositoryInfo> {

    private static final long serialVersionUID = -6829878983583733186L;

    String id;

    public RepositoryInfoDetachableModel(RepositoryInfo repoInfo) {
        super(repoInfo);
        this.id = repoInfo.getId();
    }

    @Override
    protected RepositoryInfo load() {
        try {
            return RepositoryManager.get().get(id);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}