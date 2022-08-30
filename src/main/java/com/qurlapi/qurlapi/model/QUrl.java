package com.qurlapi.qurlapi.model;

import com.qurlapi.qurlapi.util.ConstraintConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "urls")
public class QUrl {

    @Id
    @Getter
    @GeneratedValue
    private UUID id;

    @Getter
    @Column(name = "url", length = ConstraintConstants.QUrl.URL_MAX_LENGTH)
    private String url;

    @Getter
    @Column(name = "stamp", length = ConstraintConstants.QUrl.STAMP_MAX_LENGTH)
    private String stamp;

    @Getter
    @Column(name = "usages", length = ConstraintConstants.QUrl.USAGES_MAX_LENGTH)
    private int usages;

    public void use() {
        this.usages--;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final QUrl qUrl = (QUrl) o;

        return new EqualsBuilder().append(usages, qUrl.usages)
                                  .append(id, qUrl.id)
                                  .append(url, qUrl.url)
                                  .append(stamp, qUrl.stamp)
                                  .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(url)
                                          .append(stamp)
                                          .append(usages)
                                          .toHashCode();
    }

    public static class Builder {

        public QUrl instance = new QUrl();

        public QUrl.Builder withId(final UUID id) {
            instance.id = id;
            return this;
        }

        public QUrl.Builder withUrl(final String url) {
            instance.url = url;
            return this;
        }

        public QUrl.Builder withStamp(final String stamp) {
            instance.stamp = stamp;
            return this;
        }

        public QUrl.Builder withUsages(final int usages) {
            instance.usages = usages;
            return this;
        }

        public QUrl build() {
            return instance;
        }
    }
}
