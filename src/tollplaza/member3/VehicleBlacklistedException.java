package tollplaza.member3;

import java.time.LocalDateTime;


public class VehicleBlacklistedException extends Exception {

    private final String plateNumber;
    private final String reason;
    private final LocalDateTime attemptTime;

    public VehicleBlacklistedException(String plateNumber, String reason) {
        super(String.format("ALERT: Vehicle [%s] is BLACKLISTED! Reason: %s", plateNumber, reason));
        this.plateNumber = plateNumber;
        this.reason = reason;
        this.attemptTime = LocalDateTime.now();
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }
}
