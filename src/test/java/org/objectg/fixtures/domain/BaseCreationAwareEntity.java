package org.objectg.fixtures.domain;

import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * User: __nocach
 * Date: 23.10.12
 */
@MappedSuperclass
public class BaseCreationAwareEntity {
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
