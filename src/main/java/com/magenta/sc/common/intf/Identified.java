package com.magenta.sc.common.intf;

import java.io.Serializable;

/**
 * Author:  Alexandr Isaev
 * Created: 23/09/2018
 * <p/>
 * Copyright (c) 1999-2018 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 * <p/>
 * $Id$
 */
public interface Identified extends Serializable {
    Long getId();
    void setId(Long id);

    default boolean isNew() {
        return getId() == null;
    }
}
