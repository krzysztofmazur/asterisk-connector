package pl.ychu.asterisk.ami.action;

import pl.ychu.asterisk.ami.Action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Krzysztof on 2014-11-22.
 */
public class AOCMessage extends Action {
    private String channel;
    private String channelPrefix;
    private String msgType;
    private String chargeType;
    private HashMap<Integer, Integer> unitAmount;
    private HashMap<Integer, Integer> unitType;
    private String currencyName;
    private String currencyAmount;
    private String currencyMultiplier;
    private String totalType;
    private String AOCBillingId;
    private String chargingAssociationId;
    private String chargingAssociationNumber;
    private String chargingAssociationPlan;

    public AOCMessage(String channel, String channelPrefix, String msgType, String currencyName, String currencyAmount, String currencyMultiplier, String totalType, String AOCBillingId, String chargingAssociationId, String chargingAssociationNumber, String chargingAssociationPlan, String chargeType) {
        this.channel = channel;
        this.channelPrefix = channelPrefix;
        this.msgType = msgType;
        this.currencyName = currencyName;
        this.currencyAmount = currencyAmount;
        this.currencyMultiplier = currencyMultiplier;
        this.totalType = totalType;
        this.AOCBillingId = AOCBillingId;
        this.chargingAssociationId = chargingAssociationId;
        this.chargingAssociationNumber = chargingAssociationNumber;
        this.chargingAssociationPlan = chargingAssociationPlan;
        this.chargeType = chargeType;
        this.unitAmount = new HashMap<Integer, Integer>();
        this.unitType = new HashMap<Integer, Integer>();
    }

    public void addUnitAmount(Integer index, Integer value) {
        unitAmount.put(index, value);
    }

    public void addUnitType(Integer index, Integer value) {
        unitType.put(index, value);
    }

    @Override
    protected String getMessage() {
        String message;
        message = "Action: AOCMessage\n"
                + "Channel: " + channel + "\n"
                + (channelPrefix == null ? "" : "ChannelPrefix: " + channelPrefix + "\n")
                + "MsgType: " + msgType + "\n"
                + "ChargeType: " + chargeType + "\n";
        Iterator it1 = unitAmount.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry<Integer, Integer>) it1.next();
            message += "UnitAmount(" + pair.getKey() + ") :" + pair.getValue() + "\n";
        }
        Iterator it2 = unitType.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry<Integer, Integer> pair = (Map.Entry<Integer, Integer>) it2.next();
            message += "UnitType(" + pair.getKey() + ") :" + pair.getValue() + "\n";
        }
        message += (currencyName == null ? "" : "CurrencyName: " + currencyName + "\n")
                + (currencyAmount == null ? "" : "CurrencyAmount: " + currencyAmount + "\n")
                + (currencyMultiplier == null ? "" : "CurrencyMultiplier: " + currencyMultiplier + "\n")
                + (totalType == null ? "" : "TotalType: " + totalType + "\n")
                + (AOCBillingId == null ? "" : "AOCBillingId: " + AOCBillingId + "\n")
                + (chargingAssociationId == null ? "" : "ChargingAssociationId: " + chargingAssociationId + "\n")
                + (chargingAssociationNumber == null ? "" : "ChargingAssociationNumber: " + chargingAssociationNumber + "\n")
                + (chargingAssociationPlan == null ? "" : "ChargingAssociationPlan: " + chargingAssociationPlan + "\n");
        return message;
    }
}
