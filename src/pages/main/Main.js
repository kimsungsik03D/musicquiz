import React, { useState,useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios';
import { type } from '@testing-library/user-event/dist/type';

export default function Main() {
    const [userName, setUserName] = useState('')
    function onChangeUserName(e) {
        setUserName(e.target.value)
    }
  const [users, setUsers] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get(
        'http://13.125.191.54:8088/rankList'
      );
      setUsers(response.data.rankList); // 데이터는 response.data 안에 들어있습니다.
    } catch (e) {
      console.log(e);
    }
  };

    return (
        <div>
            <div>메인페이지입니다.</div>
            <div>
                <input type={'text'} name={'userName'} onChange={onChangeUserName} />{' '}
            </div>
            <div style={{ display: 'flex' }}>
                <div>
                    <Link to={'SoloGameCreate'} state={{userName : userName?userName:''}}>
                        솔로
                    </Link>
                    {/*<br/>*/}
                    {/*<Link to={'SoloGameCreate'}>솔로방만들기(임시)</Link>*/}
                </div>
                <div>
                    <Link to={'multiGameCreate'}>멀티</Link>
                </div>
                <div>
                    <Link to={'#'}>사용자설정</Link>
                </div>
            </div>
            <div>
                <Link to={'SoketTest'}>소켓테스트</Link>
            </div>
          <table border={1}>
            <thead>
            <tr>
              <th colSpan={4}>솔로모드 랭킹</th>
            </tr>
            </thead>

            <tr>
              <td>순번</td>
              <td>이름</td>
              <td>점수</td>
              <td>게임시간</td>
            </tr>
            {/*{rankListData}asdf*/}
            {/*{console.log('users',users)}*/}
            {users?users.map((data,index) => <tr
              key={index}>
                <td>{data.row}</td>
                <td>{data.username}</td>
                                        <td>{data.score}</td>

                                        <td>{(data.cleartime/1000).toFixed(2)}초</td>
                                     </tr>)
                                        :
                                        ''
            }

          </table>
        </div>
    )
}
