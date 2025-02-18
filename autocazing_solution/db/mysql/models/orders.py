from sqlalchemy import *
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from db.mysql.base import Base
import datetime

class OrderSpecifics(Base):
    __tablename__ = 'order_specifics'
    order_id = Column(Integer, ForeignKey('orders.order_id'), nullable=False, primary_key=True) # SQLAlchemy는 PK가 없는 걸 허락하지 않음. 그래서 복합 PK를 만들어줌.
    menu_name = Column(String(length=20), nullable=False, primary_key=True) # 복합 PK
    menu_quantity = Column(Integer, nullable=False)
    menu_price = Column(Integer, nullable=False)

class Orders(Base):
    __tablename__ = 'orders'
    order_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    login_id = Column(Integer, nullable=False)
    order_specifics = relationship("OrderSpecifics", cascade="all, delete-orphan")

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, server_onupdate=func.now())