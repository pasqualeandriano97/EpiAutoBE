package andrianopasquale97.EpiAutoBE.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;



    public PasswordResetToken(User user) {

        this.user = user;
        this.expiryDate = calculateExpiryDate(30);
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDateTime = now.plusMinutes(expiryTimeInMinutes);
        ZonedDateTime zonedExpiryDateTime = expiryDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedExpiryDateTime.toInstant());
    }


    public boolean isExpired() {
        Date now = new Date();
        return now.after(expiryDate);
    }
}

