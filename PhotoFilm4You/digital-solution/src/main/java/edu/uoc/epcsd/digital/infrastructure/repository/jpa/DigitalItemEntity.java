package edu.uoc.epcsd.digital.infrastructure.repository.jpa;

import edu.uoc.epcsd.digital.domain.DigitalItem;
import edu.uoc.epcsd.digital.domain.DigitalStatus;
import lombok.*;

import javax.persistence.*;

@Entity(name = "digitalitem")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalItemEntity implements DomainTranslatable<DigitalItem> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "lat", nullable = false)
    private Long lat;
    
    @Column(name = "lon", nullable = false)
    private Long lon;

    @Column(name = "link", nullable = false)
    private String link;
    
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DigitalStatus status = DigitalStatus.AVAILABLE;

    @ManyToOne(optional = false)
    private DigitalSessionEntity digitalSession;

    public static DigitalItemEntity fromDomain(DigitalItem digitalItem) {
        if (digitalItem == null) {
            return null;
        }

        return DigitalItemEntity.builder()
                .id(digitalItem.getId())
                .description(digitalItem.getDescription())
                .lat(digitalItem.getLat())
                .lon(digitalItem.getLon())
                .link(digitalItem.getLink())
                .status(digitalItem.getStatus())
                .build();
    }

    @Override
    public DigitalItem toDomain() {
        return DigitalItem.builder()
                .id(this.getId())
                .description(this.getDescription())
                .lat(this.getLat())
                .lon(this.getLon())
                .link(this.getLink())
                .status(this.getStatus())
                .digitalsessionid(this.getDigitalSession().getId())
                .build();
    }
}
