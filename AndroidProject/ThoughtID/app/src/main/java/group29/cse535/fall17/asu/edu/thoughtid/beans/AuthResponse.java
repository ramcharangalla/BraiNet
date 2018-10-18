package group29.cse535.fall17.asu.edu.thoughtid.beans;

/**
 * Created by sgollana on 11/25/2017.
 */

public class AuthResponse {
    @Override
    public String toString() {
        return "AuthResponse{" +
                "linearSVMResult=" + linearSVMResult +
                ", logisticRegressionResult=" + logisticRegressionResult +
                ", naiveBayesResult=" + naiveBayesResult +
                ", userId='" + userId + '\'' +
                '}';
    }

    public AuthResponse(Boolean linearSVMResult, Boolean logisticRegressionResult, Boolean naiveBayesResult, String userId) {
        this.linearSVMResult = linearSVMResult;
        this.logisticRegressionResult = logisticRegressionResult;
        this.naiveBayesResult = naiveBayesResult;
        this.userId = userId;
    }

    private Boolean linearSVMResult;
    private Boolean logisticRegressionResult;
    private Boolean naiveBayesResult;
    private String userId;

    public Boolean getLinearSVMResult() {
        return linearSVMResult;
    }

    public void setLinearSVMResult(Boolean linearSVMResult) {
        this.linearSVMResult = linearSVMResult;
    }

    public Boolean getLogisticRegressionResult() {
        return logisticRegressionResult;
    }

    public void setLogisticRegressionResult(Boolean logisticRegressionResult) {
        this.logisticRegressionResult = logisticRegressionResult;
    }

    public Boolean getNaiveBayesResult() {
        return naiveBayesResult;
    }

    public void setNaiveBayesResult(Boolean naiveBayesResult) {
        this.naiveBayesResult = naiveBayesResult;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
