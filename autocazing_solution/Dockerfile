FROM python:3.12.1

WORKDIR /autocazing_solution

COPY /autocazing_solution/requirements.txt /autocazing_solution/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /autocazing_solution/requirements.txt

COPY ./autocazing_solution /autocazing_solution

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8088"]