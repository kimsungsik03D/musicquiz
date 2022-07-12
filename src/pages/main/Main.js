import React, { useState,useEffect } from 'react'
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';
// import { type } from '@testing-library/user-event/dist/type';
// import testJson from 'test.json'
import { Button, Modal } from 'antd';





export default function Main(props) {
    const [userName, setUserName] = useState('')
    function onChangeUserName(e) {
        setUserName(e.target.value)
    }
  const [users, setUsers] = useState(null);
  // 화면 didMount시 Main에 User 통계를 보여주기 위한  useeffect
  useEffect(() => {
    fetchUsers();
  }, []);
  props.props.onmessage = (e) => {
    // console.log('e.data',JSON.parse(e.data))
    // setRoomId(roomId?roomId:JSON.parse(e.data).roomId)
    // console.log('e',e)
    //console.log('multiGameCreact onmessage!!', e)
  }

  //Main에 User 통계를 보여주기 위한  axios!
  const fetchUsers = async () => {
    try {
      const response = await axios.get(
        'http://52.79.186.223:8088/rankList'
      );
      setUsers(response.data.rankList); // 데이터는 response.data 안에 들어있습니다.
    } catch (e) {
      //console.log(e);
    }
  };
  //잔디에 메세지 보내는 로직임 뮤직퀴즈와는 상관없는 로직임
  const Jandi = () => {
    axios(
      {
        method:'post',
        url: 'https://wh.jandi.com/connect-api/webhook/21346604/32163b66eafdc004a7a0d6f38f7f2186',
        // Accept: 'application/vnd.tosslab.jandi-v2+json',
        // ContentType: 'application/json',
        data:{
          "body" : "잔디 메시지 테스트입니다",
          "connectColor" : "#FAC11B",
          "connectInfo" : [{
            "title" : "메렁1",
            "description" : "메렁2"
          },
          {
            "title": "메렁3",
            "description": "메렁4",
            "imageUrl": "http://www.naver.com"
          }
            ]
        }
      }
    ).then(function (response) {
      console.log(response);
    })
      .catch(function (error) {
        console.log(error);
      });
    
  }


  //antd을통해 modal창 구현으로 컴포넌트 분리.
  const CustomModal = (props) =>{
    // const userName = useLocation().state;
    // console.log('!#$%#%$^@^@#$', userName);
    const [roomid,setRoomId] = useState()
    const [loading, setLoading] = useState(false);
    const [visible, setVisible] = useState(false);

    const showModal = () => {
      setVisible(true);
    };

    const handleOk = () => {
      setLoading(true);
      setTimeout(() => {
        setLoading(false);
        setVisible(false);
      }, 3000);
    };

    const handleCancel = () => {
      setVisible(false);
    };
    const sendMultiCreInitData = () =>{
      const data ={roomStatus:'create'}
      props.props.send(JSON.stringify({roomStatus:'create'}))
    }
    const sendMultiINtoInitData = () =>{
      const data ={roomStatus:'create'}
      //console.log('roomid!@#$$%#&^$^@# ',roomid);
      props.props.send(JSON.stringify({roomStatus:'enter',roomId:roomid}))
    }
    // console.log("!@#!@#!@#!@#2", userName);
    return (
      <>
        <Button type="primary" onClick={showModal}>
          멀티모드
        </Button>
        <Modal
          visible={visible}
          title="멀티모드입장"
          onOk={handleOk}
          onCancel={handleCancel}
          footer={null}
          // footer={[
          //   <Button key="back" onClick={handleCancel}>
          //     Return
          //   </Button>,
          //   <Button key="submit" type="primary" loading={loading} onClick={handleOk}>
          //     Submit
          //   </Button>,
          // ]}
        >
          <div className={'multimodetype'}>
            <Link to={'multiGameCreate'} state={{usertype:'guest',roomid : roomid, userName : userName?userName:''}}><div onClick={sendMultiINtoInitData}>방입장</div></Link><input type='text' onChange={e=>{setRoomId(e.target.value)}}/>
            <Link to={'multiGameCreate'} state={{usertype:'host',userName : userName?userName:''}}><div onClick={sendMultiCreInitData}>방생성</div></Link>
          </div>
        </Modal>
      </>
    );

  }

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
                  {/* 폰 파일에 따로 정의 되어있음 */}
                    <Link to={'#'}><CustomModal props={props.props} /></Link>
                </div>
            </div>
            <div>
                <Link to={'SoketTest'}>소켓테스트</Link>
              {/*//잔디에 메세지 보내는 로직임 뮤직퀴즈와는 상관없는 로직임*/}
              <div onClick={Jandi}>잔디 커넥트</div>
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

