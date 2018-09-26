import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Customer extends React.Component<ICustomerProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { customerList, match } = this.props;
    return (
      <div>
        <h2 id="customer-heading">
          <Translate contentKey="abstraktApp.customer.home.title">Customers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="abstraktApp.customer.home.createLabel">Create new Customer</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.frequency">Frequency</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.paymentMethod">Payment Method</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.flatPosition">Flat Position</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.number">Number</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.street">Street</Translate>
                </th>
                <th>
                  <Translate contentKey="abstraktApp.customer.area">Area</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.name}</td>
                  <td>{customer.email}</td>
                  <td>{customer.phone}</td>
                  <td>
                    <Translate contentKey={`abstraktApp.Frequency.${customer.frequency}`} />
                  </td>
                  <td>
                    <Translate contentKey={`abstraktApp.PaymentMethod.${customer.paymentMethod}`} />
                  </td>
                  <td>{customer.flatPosition}</td>
                  <td>{customer.number}</td>
                  <td>{customer.street}</td>
                  <td>{customer.area ? <Link to={`area/${customer.area.id}`}>{customer.area.name}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${customer.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customer.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${customer.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ customer }: IRootState) => ({
  customerList: customer.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Customer);
