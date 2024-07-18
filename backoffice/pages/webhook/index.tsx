import type { NextPage } from 'next';
import Link from 'next/link';
import { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import ReactPaginate from 'react-paginate';

import ModalDeleteCustom from '@commonItems/ModalDeleteCustom';
import { handleDeletingResponse } from '@commonServices/ResponseStatusHandlingService';
import type { Webhook } from '@webhookModels/Webhook';
import { deleteWebhook, getWebhooks } from '@webhookServices/WebhookService';
import {
  DEFAULT_PAGE_NUMBER,
  DEFAULT_PAGE_SIZE,
  SYSTEM_WEBHOOKS_URL
} from 'constants/Common';

const WebhookList: NextPage = () => {
  const [webhookClassIdWantToDelete, setWebhookIdWantToDelete] = useState<number>(-1);
  const [webhookClassNameWantToDelete, setWebhookNameWantToDelete] = useState<string>('');
  const [showModalDelete, setShowModalDelete] = useState<boolean>(false);
  const [webhookClasses, setWebhookes] = useState<Webhook[]>([]);
  const [isLoading, setLoading] = useState(false);
  const [pageNo, setPageNo] = useState<number>(DEFAULT_PAGE_NUMBER);
  const [totalPage, setTotalPage] = useState<number>(1);

  const handleClose: any = () => setShowModalDelete(false);
  const handleDelete: any = () => {
    if (webhookClassIdWantToDelete == -1) {
      return;
    }
    deleteWebhook(webhookClassIdWantToDelete)
      .then((response) => {
        setShowModalDelete(false);
        handleDeletingResponse(response, webhookClassNameWantToDelete);
        setPageNo(DEFAULT_PAGE_NUMBER);
        getListWebhook();
      })
      .catch((error) => console.log(error));
  };

  const getListWebhook = () => {
    getWebhooks(pageNo, DEFAULT_PAGE_SIZE)
      .then((data) => {
        setTotalPage(data.totalPages);
        setWebhookes(data.webhookClassContent);
        setLoading(false);
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    setLoading(true);
    getListWebhook();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [pageNo]);

  const changePage = ({ selected }: any) => {
    setPageNo(selected);
  };

  if (isLoading) return <p>Loading...</p>;
  if (!webhookClasses) return <p>No Tax Class</p>;
  return (
    <>
      <div className="row mt-5">
        <div className="col-md-8">
          <h2 className="text-danger font-weight-bold mb-3">Tax Class</h2>
        </div>
        <div className="col-md-4 text-right">
          <Link href={`${SYSTEM_WEBHOOKS_URL}/create`}>
            <Button>Create Tax Class</Button>
          </Link>
        </div>
      </div>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Payload Url</th>
            <th>Secret</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {webhookClasses.map((webhookClass) => (
            <tr key={webhookClass.id}>
              <td>{webhookClass.id}</td>
              <td>{webhookClass.payloadUrl}</td>
              <td>{webhookClass.secret}</td>
              <td>{webhookClass.status}</td>
              <td>
                <Link href={`${SYSTEM_WEBHOOKS_URL}/${webhookClass.id}/edit`}>
                  <button className="btn btn-outline-primary btn-sm" type="button">
                    Edit
                  </button>
                </Link>
                &nbsp;
                <button
                  className="btn btn-outline-danger btn-sm"
                  type="button"
                  onClick={() => {
                    setShowModalDelete(true);
                    setWebhookIdWantToDelete(webhookClass.id);
                    setWebhookNameWantToDelete(webhookClass.payloadUrl);
                  }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <ModalDeleteCustom
        showModalDelete={showModalDelete}
        handleClose={handleClose}
        nameWantToDelete={webhookClassNameWantToDelete}
        handleDelete={handleDelete}
        action="delete"
      />
      <ReactPaginate
        forcePage={pageNo}
        previousLabel={'Previous'}
        nextLabel={'Next'}
        pageCount={totalPage}
        onPageChange={changePage}
        containerClassName={'pagination-container'}
        previousClassName={'previous-btn'}
        nextClassName={'next-btn'}
        disabledClassName={'pagination-disabled'}
        activeClassName={'pagination-active'}
      />
    </>
  );
};

export default WebhookList;
