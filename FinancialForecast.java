import java.util.*;

public class FinancialForecast {
    
    private float currentBalance = 0;
    private GregorianCalendar currentDate = null;
    private HashMap<String, String> paySchedule = null;
    private HashMap<String, String> billSchedule = null;
    private HashMap<String, String> billName = null;

    public static void main(String[] args){
	String returnValue = "";
	float currentBalanceFloat = Float.parseFloat(args[0]);
	
	HashMap<String, String> billScheduleHash = new HashMap<String, String>();

	//day of the month, bill amount
	billScheduleHash.put("1", "300");

	HashMap<String, String> billName = new HashMap<String,String>();

	//day of the month, bill name
        billName.put("1", "Liberty Mutual (car insurance) and Wells Fargo CC");

	HashMap<String, String> payScheduleHash = new HashMap<String, String>();

	//day of the month, pay amount
        payScheduleHash.put("2018-1-1", "2000")

	FinancialForecast financialForecast = new FinancialForecast(currentBalanceFloat, billScheduleHash, billName, payScheduleHash);
	returnValue = financialForecast.getFutureBalance();
	System.out.println(returnValue);
    }
    
    public FinancialForecast(float currentBalance, HashMap<String,String> billSchedule, HashMap<String, String> billName, HashMap<String, String> paySchedule){
	this.currentBalance = currentBalance;
	this.currentDate = new GregorianCalendar();
	this.billSchedule = billSchedule;
	this.billName = billName;
	this.paySchedule = paySchedule;
    }
    
    public float getCurrentBalance(){
	return currentBalance;
    }
    
    public GregorianCalendar getCurrentDate(){
	return currentDate;
    }
    
    public HashMap<String, String> getBillSchedule(){
	return billSchedule;
    }

    public HashMap<String, String> getPaySchedule(){
	return paySchedule;
    }
    
    public static GregorianCalendar convertDateStringToGregorianCalendar(String dateString){
	GregorianCalendar convertedDate = null;
	String[] dateArray = dateString.split("-");
	if(dateArray.length == 3){
	    convertedDate = new GregorianCalendar(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]));
	}
	return convertedDate;
    }
    
    public String getFutureBalance(){
	String returnValue = "\nBelow is a report on future balances:\n\n";
	returnValue += "Date\t\tBalance\t\tCost/Pay\tName\n";
	GregorianCalendar rollingGregorian = (GregorianCalendar)currentDate.clone();
	int rollingYear = 0;
	int rollingMonth = 0;
	int rollingDate = 0;
	float futureBalance = currentBalance;
	int days = 0;
	float totalCost = 0;
	float totalPay = 0;

	while(days < 367){
	//	for(int i=0; i < (paySchedule.size()*8); i++){
	    rollingYear = rollingGregorian.get(rollingGregorian.YEAR);
	    rollingMonth = rollingGregorian.get(rollingGregorian.MONTH);
	    rollingDate = rollingGregorian.get(rollingGregorian.DATE);

	    String dateString = rollingYear + "-" + (rollingMonth+1) + "-" + rollingDate;

	    if(rollingDate == 1){
		returnValue += "\nMonthly total:  "+Float.toString(totalPay)+" - "+Float.toString(totalCost)+" = "+Float.toString(totalPay-totalCost)+"\n";
		totalCost = 0;
		totalPay = 0;
		returnValue += "==========================\n";
	    }

	    if(billSchedule.containsKey(Integer.toString(rollingDate))){
		if(!dateString.equals("2015-7-2")){
		    String cost = billSchedule.get(Integer.toString(rollingDate));
		    returnValue += dateString + ":\t";
		    futureBalance -= Float.parseFloat(cost);
		    returnValue += futureBalance + "\t\t";
		    returnValue += "-" + cost + "\t\t" + billName.get(Integer.toString(rollingDate)) + "\n";
		    totalCost += Float.parseFloat(cost);
		}
	    }

	    if(paySchedule.containsKey(dateString)){
		String pay = paySchedule.get(dateString);
		paySchedule.remove(pay);
		returnValue += dateString + ":\t";
		futureBalance += Float.parseFloat(pay);
		returnValue += futureBalance + "\t\t";
                returnValue += "+" + pay + "\n";
		totalPay += Float.parseFloat(pay);
	    }

	    if(rollingDate == rollingGregorian.getActualMaximum(rollingGregorian.DATE)){ 
		if(rollingMonth == rollingGregorian.getActualMaximum(rollingGregorian.MONTH)){
                    rollingGregorian.set(rollingGregorian.DATE, 1);
		    rollingGregorian.set(rollingGregorian.MONTH, 0);
		    rollingGregorian.roll(rollingGregorian.YEAR, true);
		} else {
		    rollingGregorian.set(rollingGregorian.DATE, 1);
                    rollingGregorian.roll(rollingGregorian.MONTH, true);
		}
	    } else {
		rollingGregorian.roll(rollingGregorian.DATE, true);
	    }

	    days++;
	}

	return returnValue;
    }
    
}
