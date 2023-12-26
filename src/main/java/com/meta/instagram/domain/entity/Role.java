package com.meta.instagram.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

public enum Role {
    ROLE_USER, ROLE_MANAGER, ROLE_ADMIN
}
