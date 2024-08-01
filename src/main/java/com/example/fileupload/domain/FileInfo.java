package com.example.fileupload.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
public class FileInfo {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name="file_uploaded_at", nullable=false)
    private LocalDateTime uploadedAt;

    @Column(name="file_uploader_ip_address", nullable=false)
    private String uploaderIpAddress;

    @Column(name="file_stored_url", nullable=false)
    private String storedUrl;

    @Column(name="file_original_name", nullable=false)
    private String originalName;

    @Column(name="file_extension", nullable = false)
    private String extension;

    @Column(name="file_size", nullable=false)
    private Long fileSize;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        FileInfo fileInfo = (FileInfo) o;
        return getId() != null && Objects.equals(getId(), fileInfo.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
