package sample.Common;

import java.io.Serializable;

public class TransferPackage implements Serializable {

    private long serialVersionUID = 3L;
    private final String operation;
    private final String content;

    public TransferPackage(String operation, String content){
        this.operation = operation;
        this.content = content;
    }

    public String getOperation() {
        return operation;
    }

    public String getContent() {
        return content;
    }
}
