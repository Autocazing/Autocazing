from sqlalchemy import *
from sqlalchemy.orm import relationship, declarative_base
from sqlalchemy.sql import func

Base = declarative_base()

class OrderSpecific(Base):
    __tablename__ = 'order_specifics'
    order_id = Column(Integer, ForeignKey('orders.order_id'), nullable=False)
    menu_id = Column(Integer, nullable=False)
    menu_quantity = Column(Integer, nullable=False)
    menu_price = Column(Integer, nullable=False)

class Orders(Base):
    __tablename__ = 'orders'
    order_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    store_id = Column(Integer, nullable=False)
    order_specifics = relationship("OrderSpecific", back_populates="order", cascade="all, delete-orphan")

    created_at = Column(DateTime, default=func.now(), nullable=False)
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())