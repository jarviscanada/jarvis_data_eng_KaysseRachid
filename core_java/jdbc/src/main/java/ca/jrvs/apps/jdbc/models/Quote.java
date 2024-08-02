package ca.jrvs.apps.jdbc.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.sql.Timestamp;
import java.sql.Date;

public class Quote
{

    private String ticker; //id
    private double open;
    private double high;
    private double low;
    private double price;
    private int volume;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date latestTradingDay;
    private double previousClose;
    private double change;
    private String changePercent;
    private Timestamp timestamp; //time when the info was pulled

    public Quote() {}

    public Quote(String ticker, double open, double high, double low, double price, int volume, Date latestTradingDay, double previousClose, double change, String changePercent, Timestamp timestamp)
    {
        this.ticker = ticker;
        this.open = open;
        this.high = high;
        this.low = low;
        this.price = price;
        this.volume = volume;
        this.latestTradingDay = latestTradingDay;
        this.previousClose = previousClose;
        this.change = change;
        this.changePercent = changePercent;
        this.timestamp = timestamp;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public void setOpen(double open)
    {
        this.open = open;
    }

    public void setHigh(double high)
    {
        this.high = high;
    }

    public void setLow(double low)
    {
        this.low = low;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    public void setLatestTradingDay(Date latestTradingDay)
    {
        this.latestTradingDay = latestTradingDay;
    }

    public void setPreviousClose(double previousClose)
    {
        this.previousClose = previousClose;
    }

    public void setChange(double change)
    {
        this.change = change;
    }

    public void setChangePercent(String changePercent)
    {
        this.changePercent = changePercent;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTicker()
    {
        return ticker;
    }

    public double getOpen()
    {
        return open;
    }

    public double getHigh()
    {
        return high;
    }

    public double getLow()
    {
        return low;
    }

    public double getPrice()
    {
        return price;
    }

    public int getVolume()
    {
        return volume;
    }

    public Date getLatestTradingDay()
    {
        return latestTradingDay;
    }

    public double getPreviousClose()
    {
        return previousClose;
    }

    public double getChange()
    {
        return change;
    }

    public String getChangePercent()
    {
        return changePercent;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    @Override
    public String toString()
    {
        return "symbol='" + ticker + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", price=" + price +
                ", volume=" + volume +
                ", latestTradingDay=" + latestTradingDay +
                ", previousClose=" + previousClose +
                ", change=" + change +
                ", changePercent='" + changePercent + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
