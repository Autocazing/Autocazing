from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from ..core.config import get_setting
from .base import Base

settings = get_setting()    # config.py에 설정해놓은 setting을 가져옴

SQLALCHEMY_DATABASE_URL = 'mysql+pymysql://{}:{}@{}:{}/{}'.format(
    settings.MYSQL_USER,
    settings.MYSQL_PASSWORD,
    settings.MYSQL_HOST,
    settings.MYSQL_PORT,
    settings.MYSQL_DATABASE
)

db_engine = create_engine(SQLALCHEMY_DATABASE_URL)  # data base에 연결할 엔진 생성

# 개발 환경에서만 테이블 자동 생성
if settings.ENVIRONMENT == "local":
    Base.metadata.create_all(db_engine)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=db_engine)  # 위의 엔진을 이용해 세션 팩토리 생성