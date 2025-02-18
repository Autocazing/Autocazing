from sqlalchemy import *
from sqlalchemy.sql import func
from db.mysql.base import Base

class Menus(Base):
    __tablename__ = 'menus'
    menu_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    login_id = Column(Integer, nullable=False)

    menu_name = Column(String(length=20), nullable=False)
    menu_price = Column(Integer, nullable=False)
    on_event = Column(Boolean, nullable=False)
    discount_rate = Column(Integer, nullable=False)

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())