from sqlalchemy import *
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from db.mysql.base import Base

class MenuSalesSpecific(Base):
    __tablename__ = 'menu_sales_specific'

class ExpirationSpecific(Base):
    __tablename__ = 'expiration_specific'

class Reports(Base):
    __tablename__ = 'reports'