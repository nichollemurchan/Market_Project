// Singleton order manager uses quickfixj as fix engine
package market.rest;
import quickfix.ConfigError;
import quickfix.Dictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionFactory;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;

public class OrderManager
{
    // The singleton instance
    private static OrderManager instance = null;

    private final QuickFixConfigBuilder qfConfig;
    private FixEngine engine = null;
    private SocketInitiator fixConnection = null;

    private OrderManager() // private so can't be instantiated
    {
        qfConfig = new QuickFixConfigBuilder();
        engine = new FixEngine();
        MessageStoreFactory messageStoreFactory = new FileStoreFactory (qfConfig.GetFileStoreSettings());
        LogFactory logFactory = new ScreenLogFactory (true, true, true);
        quickfix.fix42.MessageFactory messageFactory = new quickfix.fix42.MessageFactory();

        try
        {
            fixConnection = new SocketInitiator (engine, messageStoreFactory,
                                                 qfConfig.GetSessionSettings(),
                                                 logFactory, messageFactory);
            fixConnection.start(); // This starts the connection in another thread, so it doesn't block
        }
        catch (quickfix.ConfigError ce)
        {
            System.out.println ("Error setting socket acceptor < " + ce.getMessage() + " >");
        }
    }

    public static OrderManager getInstance()
    {
        if (instance == null)
            instance = new OrderManager();
        return instance;
    }

    public class OrderResult
    {
        double price;
        int shares;
        boolean longPosition;
    }

    // Buy a stock
    public OrderResult buyOrder(String stock, double price, int shares)
    {
        OrderResult o = new OrderResult();
        o.price = price;
        o.shares = shares;
        o.longPosition = true;
        engine.sendNewBuyOrder (stock, price, shares);
        return o;
    }

    // Sell a stock
    public OrderResult sellOrder(String stock, double price, int shares)
    {
        OrderResult o = new OrderResult();
        o.price = price;
        o.shares = shares;
        o.longPosition = false;
        engine.sendNewSellOrder (stock, price, shares);
        return o;
    }
}


class FixEngine extends quickfix.fix42.MessageCracker implements quickfix.Application
{

    private SessionID sessId = null;
    public FixEngine ()
    {

    }
    
    public void onCreate (SessionID sessionID)
    {
        sessId = sessionID;
    }

    public void onLogon (SessionID sessionID)
    {

    }

    public void onLogout (SessionID sessionID)
    {

    }

    public void toAdmin (quickfix.Message message, SessionID sessionID)
    {

    }

    public void toApp (quickfix.Message message, SessionID sessionID) 
        throws DoNotSend
    {

    }

    public void fromAdmin (quickfix.Message message, SessionID sessionID)
        throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon
    {

    }

    public void fromApp(quickfix.Message message, SessionID sessionID) 
        throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType
    {
        crack (message, sessionID); 
    }

    // crack handlers, only care about execution reports
    public void onMessage (quickfix.fix42.ExecutionReport message, SessionID sessionID)
          throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue 
    {
        /* Iterate through all fields in fix message and stored to DB?
           We wind up doing this in the OrderBroker ...
            
        Iterator fieldIterator = message.iterator();
        while (fieldIterator.hasNext()) 
        {
            Field field = (Field)fieldIterator.next();
            String tag = Integer.toString(field.getTag());
            String value = message.getString(field.getTag());
        }
        */
        
        OrdStatus ordStatus = new OrdStatus();
        message.get(ordStatus);
        if (ordStatus.valueEquals (OrdStatus.PARTIALLY_FILLED) || 
            ordStatus.valueEquals (OrdStatus.FILLED))
        {
            LastShares ls = new LastShares();
            LastPx px = new LastPx();
            Side side = new Side();
            message.get(ls);
            message.get(px);
            message.get(side);
            // Optional what you want todo with this info
        }
    }

    // Send new buy limit order to exchange 
    public void sendNewBuyOrder (String stock, double px, int shares)
    {
        sendNewOrder (true, stock, px, shares);
    }

    // Send new sell limit order to exchange
    public void sendNewSellOrder (String stock, double px, int shares)
    {
        sendNewOrder (false, stock, px, shares);
    }

    public void sendNewOrder (boolean buy, String stock, double px, int shares)
    {
        ClOrdID orderId    = new ClOrdID("EQUITY-TRADER-" + System.currentTimeMillis());
        Side s             = null;
        HandlInst handInst = new HandlInst(HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE); 
        TransactTime tt    = new TransactTime();
        OrdType o          = new OrdType(OrdType.LIMIT);
        Symbol sym         = new Symbol (stock);
        Price price        = new Price (px);
        OrderQty qty       = new OrderQty (shares);

        if (buy == true)
            s = new Side (Side.BUY);
        else
            s = new Side (Side.SELL);

        NewOrderSingle no = new NewOrderSingle (orderId, handInst, sym, s, tt, o);
        no.set (price);
        no.set (qty);
        
        try 
        {
            Session.sendToTarget(no, sessId);
        } 
        catch (SessionNotFound e) 
        {
        }
    }
}

class QuickFixConfigBuilder
{
    public QuickFixConfigBuilder ()
    {
        fileStoreSettings = new SessionSettings();
        fileStoreSettings.setString (FileStoreFactory.SETTING_FILE_STORE_PATH, "fixMsgStore");

        settings = new SessionSettings();
        settings.setString (SessionFactory.SETTING_CONNECTION_TYPE, SessionFactory.INITIATOR_CONNECTION_TYPE);
        settings.setString (Session.SETTING_START_TIME, "00:00:00");
        settings.setString (Session.SETTING_END_TIME, "00:00:00");
        settings.setString (Session.SETTING_NON_STOP_SESSION, "Y");
        settings.setString (Session.SETTING_USE_DATA_DICTIONARY, "N");
        settings.setLong   (Session.SETTING_HEARTBTINT, 30);

        Dictionary sessionDict = new Dictionary();
        sessionDict.setString (SessionSettings.BEGINSTRING, "FIX.4.2");
        sessionDict.setString (SessionSettings.SENDERCOMPID, "EQUITY-TRADER");
        sessionDict.setString (SessionSettings.TARGETCOMPID, "NASDAQ");
        sessionDict.setString (Initiator.SETTING_SOCKET_CONNECT_HOST, "127.0.0.1");
        sessionDict.setString (Initiator.SETTING_SOCKET_CONNECT_PORT, "9898");
        sessionDict.setString (Session.SETTING_RESET_ON_LOGON, "Y");

        quickfix.SessionID sessId = new quickfix.SessionID (new quickfix.field.BeginString ("FIX.4.2"),
                                                            new quickfix.field.SenderCompID ("EQUITY-TRADER"),
                                                            new quickfix.field.TargetCompID ("NASDAQ"));
        try
        {
            settings.set (sessId, sessionDict);
        }
        catch (ConfigError ce)
        {
            System.out.println ("Error setting quickfix settings < " + ce.getMessage() + " >");
        }

    }

    public SessionSettings GetSessionSettings ()
    {
        return settings;
    }

    public SessionSettings GetFileStoreSettings ()
    {
        return fileStoreSettings;
    }

    private final SessionSettings settings;
    private final SessionSettings fileStoreSettings;
}


