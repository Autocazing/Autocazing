from pydantic import BaseSettings

class Settings(BaseSettings):   # env 파일에 작성한 내용을 읽어옴
    MYSQL_HOST: str
    MYSQL_PORT: int
    MYSQL_USER: str
    MYSQL_PASSWORD: str
    MYSQL_DATABASE: str

    class Config:
        env_file = ".env"

def get_setting():
    return Settings()