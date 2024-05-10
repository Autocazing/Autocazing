from fastapi import FastAPI, BackgroundTasks
from fastapi.middleware.cors import CORSMiddleware
import predict

app = FastAPI();

# CORS 전역 설정
origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# 일단은 이데로 냅두는데 나중에 request 시간 많이 걸리면 backgroundtask 로 빼야함
@app.post('/predict/revenue')
async def revenue(background_tasks: BackgroundTasks):
    Date = []
    Value = []

    Date,Value = predict.predict_revenue();

    # 디버깅용
    print(Date)
    print(Value)

    return {"message" : "Predice Revenue Complete"}

# 나중에 포트 받으면 port=???? 이렇게 하면 됨
if __name__ == '__main__':
    import uvicorn
    uvicorn.run(app="train:app",host='0.0.0.0', reload=True)