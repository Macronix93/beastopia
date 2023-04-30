package de.uniks.beastopia.teaml.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Message
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_BODY = "body";
   public static final String PROPERTY_SENDER = "sender";
   private String createdAt;
   private String updatedAt;
   private String id;
   private String body;
   protected PropertyChangeSupport listeners;
   private User sender;

   public String getCreatedAt()
   {
      return this.createdAt;
   }

   public Message setCreatedAt(String value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final String oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

   public String getUpdatedAt()
   {
      return this.updatedAt;
   }

   public Message setUpdatedAt(String value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final String oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

   public String getId()
   {
      return this.id;
   }

   public Message setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
      return this;
   }

   public String getBody()
   {
      return this.body;
   }

   public Message setBody(String value)
   {
      if (Objects.equals(value, this.body))
      {
         return this;
      }

      final String oldValue = this.body;
      this.body = value;
      this.firePropertyChange(PROPERTY_BODY, oldValue, value);
      return this;
   }

   public User getSender()
   {
      return this.sender;
   }

   public Message setSender(User value)
   {
      if (this.sender == value)
      {
         return this;
      }

      final User oldValue = this.sender;
      this.sender = value;
      this.firePropertyChange(PROPERTY_SENDER, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getCreatedAt());
      result.append(' ').append(this.getUpdatedAt());
      result.append(' ').append(this.getId());
      result.append(' ').append(this.getBody());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setSender(null);
   }
}