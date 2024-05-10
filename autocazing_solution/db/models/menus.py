from sqlalchemy import *
from sqlalchemy.orm import relationship, declarative_base
from sqlalchemy.sql import func
from db.base import Base

class Menus(Base):
    __tablename__ = 'menus'
    menu_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    store_id = Column(Integer, nullable=False)

    menu_name = Column(String(length=20), nullable=False)
    menu_price = Column(Integer, nullable=False)
    on_event = Column(Boolean, nullable=False)
    discount_rate = Column(Integer, nullable=False)

    created_at = Column(DateTime, default=func.now(), nullable=False)
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())